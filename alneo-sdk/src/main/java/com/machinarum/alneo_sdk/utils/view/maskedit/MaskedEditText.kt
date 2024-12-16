package com.machinarum.alneo_sdk.utils.view.maskedit

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.machinarum.alneo_sdk.R

class MaskedEditText : AppCompatEditText, TextWatcher {
    private var mask: String? = null
    private var charRepresentation = 0.toChar()
    private var keepHint = false
    private var rawToMask: IntArray = IntArray(100)
    private var rawText: RawText = RawText()
    private var editingBefore = false
    private var editingOnChanged = false
    private var editingAfter = false
    private var maskToRaw: IntArray = IntArray(100)
    private var selectionIndex = 0
    private var initialized = false
    private var ignore = false
    private var maxRawLength = 0
    private var lastValidMaskPosition = 0
    private var selectionChanged = false
    private var focusChangeListener: OnFocusChangeListener? = null
    private var allowedChars: String? = null
    private var deniedChars: String? = null
    var isKeepingText = false
        private set

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()

        context.obtainStyledAttributes(attrs, R.styleable.MaskedEditText).apply {
            mask = getString(R.styleable.MaskedEditText_mask)
            allowedChars = getString(R.styleable.MaskedEditText_allowed_chars)
            deniedChars = getString(R.styleable.MaskedEditText_denied_chars)

            val representation =
                getString(R.styleable.MaskedEditText_char_representation)

            charRepresentation =
                if (representation == null) '#' else representation[0]

            keepHint = getBoolean(R.styleable.MaskedEditText_keep_hint, false)

            cleanUp()
            recycle()
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superParcelable = super.onSaveInstanceState()
        val state = Bundle()
        state.putParcelable("super", superParcelable)
        state.putString("text", getRawText())
        state.putBoolean("keepHint", isKeepHint())
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val bundle = state as Bundle
        keepHint = bundle.getBoolean("keepHint", false)
        super.onRestoreInstanceState(state.getParcelable("super"))
        val text = bundle.getString("text")
        setText(text)
    }

    override fun setText(text: CharSequence, type: BufferType) {
//		if (text == null || text.equals("")) return;
        super.setText(text, type)
    }

    /**
     * @param listener - its onFocusChange() method will be called before performing MaskedEditText operations,
     * related to this event.
     */
    override fun setOnFocusChangeListener(listener: OnFocusChangeListener) {
        focusChangeListener = listener
    }

    private fun cleanUp() {
        initialized = false

        if (mask.isNullOrEmpty())
            return

        generatePositionArrays()

        if (!isKeepingText) {
            rawText = RawText()
            selectionIndex = rawToMask[0]
        }

        editingBefore = true
        editingOnChanged = true
        editingAfter = true
        if (hasHint() && rawText.length() == 0) {
            this.setText(makeMaskedTextWithHint())
        } else {
            this.setText(makeMaskedText())
        }
        editingBefore = false
        editingOnChanged = false
        editingAfter = false
        maxRawLength = maskToRaw[previousValidPosition(mask!!.length - 1)] + 1
        lastValidMaskPosition = findLastValidMaskPosition()
        initialized = true
        super.setOnFocusChangeListener { v, hasFocus ->
            if (focusChangeListener != null) {
                focusChangeListener!!.onFocusChange(v, hasFocus)
            }
            if (hasFocus()) {
                selectionChanged = false
                this@MaskedEditText.setSelection(lastValidPosition())
            }
        }
    }

    private fun findLastValidMaskPosition(): Int {
        for (i in maskToRaw.indices.reversed()) {
            if (maskToRaw[i] != -1) return i
        }
        throw RuntimeException("Mask must contain at least one representation char")
    }

    private fun hasHint(): Boolean {
        return hint != null
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!, attrs, defStyle
    ) {
        init()
    }

    fun setShouldKeepText(shouldKeepText: Boolean) {
        isKeepingText = shouldKeepText
    }

    fun setMask(mask: String?) {
        this.mask = mask
        cleanUp()
    }

    fun getMask(): String? {
        return mask
    }

    fun getRawText(): String {
        return rawText.text
    }

    fun setCharRepresentation(charRepresentation: Char) {
        this.charRepresentation = charRepresentation
        cleanUp()
    }

    fun getCharRepresentation(): Char {
        return charRepresentation
    }

    /**
     * Generates positions for values characters. For instance:
     * Input data: mask = "+7(###)###-##-##
     * After method execution:
     * rawToMask = [3, 4, 5, 6, 8, 9, 11, 12, 14, 15]
     * maskToRaw = [-1, -1, -1, 0, 1, 2, -1, 3, 4, 5, -1, 6, 7, -1, 8, 9]
     * charsInMask = "+7()- " (and space, yes)
     */
    private fun generatePositionArrays() {
        val aux = IntArray(mask!!.length)
        maskToRaw = IntArray(mask!!.length)
        var charsInMaskAux = ""
        var charIndex = 0
        for (i in 0 until mask!!.length) {
            val currentChar = mask!![i]
            if (currentChar == charRepresentation) {
                aux[charIndex] = i
                maskToRaw[i] = charIndex++
            } else {
                val charAsString = currentChar.toString()
                if (!charsInMaskAux.contains(charAsString)) {
                    charsInMaskAux += charAsString
                }
                maskToRaw[i] = -1
            }
        }
        if (charsInMaskAux.indexOf(' ') < 0) {
            charsInMaskAux += SPACE
        }
        charsInMaskAux.toCharArray()
        rawToMask = IntArray(charIndex)
        System.arraycopy(aux, 0, rawToMask, 0, charIndex)
    }

    private fun init() {
        addTextChangedListener(this)
    }

    override fun beforeTextChanged(
        s: CharSequence, start: Int, count: Int,
        after: Int
    ) {
        if (!editingBefore) {
            editingBefore = true
            if (start > lastValidMaskPosition) {
                ignore = true
            }
            var rangeStart = start
            if (after == 0) {
                rangeStart = erasingStart(start)
            }
            val range = calculateRange(rangeStart, start + count)
            if (range.start != -1) {
                rawText.subtractFromString(range)
            }
            if (count > 0) {
                selectionIndex = previousValidPosition(start)
            }
        }
    }

    private fun erasingStart(s: Int): Int {
        var start = s

        while (start > 0 && maskToRaw[start] == -1) {
            start--
        }
        return start
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, c: Int) {
        var count = c
        if (!editingOnChanged && editingBefore) {
            editingOnChanged = true
            if (ignore) {
                return
            }
            if (count > 0) {
                val startingPosition = maskToRaw[nextValidPosition(start)]
                val addedString = s.subSequence(start, start + count).toString()
                count = rawText.addToString(clear(addedString), startingPosition, maxRawLength)
                if (initialized) {
                    val currentPosition: Int = if (startingPosition + count < rawToMask.size) rawToMask[startingPosition + count] else lastValidMaskPosition + 1
                    selectionIndex = nextValidPosition(currentPosition)
                }
            }
        }
    }

    override fun afterTextChanged(s: Editable) {
        if (!editingAfter && editingBefore && editingOnChanged) {
            editingAfter = true
            if (hasHint() && (keepHint || rawText.length() == 0)) {
                setText(makeMaskedTextWithHint())
            } else {
                setText(makeMaskedText())
            }
            selectionChanged = false
            setSelection(selectionIndex)
            editingBefore = false
            editingOnChanged = false
            editingAfter = false
            ignore = false
        }
    }

    fun isKeepHint(): Boolean {
        return keepHint
    }

    fun setKeepHint(keepHint: Boolean) {
        this.keepHint = keepHint
        setText(getRawText())
    }

    override fun onSelectionChanged(selectionStart: Int, selectionEnd: Int) {
        // On Android 4+ this method is being called more than 1 time if there is a hint in the EditText, what moves the cursor to left
        // Using the boolean var selectionChanged to limit to one execution
        var selStart = selectionStart
        var selEnd = selectionEnd
        if (initialized) {
            if (!selectionChanged) {
                selStart = fixSelection(selStart)
                selEnd = fixSelection(selEnd)

                // exactly in this order. If getText.length() == 0 then selStart will be -1
                if (selStart > text!!.length) selStart = text!!.length
                if (selStart < 0) selStart = 0

                // exactly in this order. If getText.length() == 0 then selEnd will be -1
                if (selEnd > text!!.length) selEnd = text!!.length
                if (selEnd < 0) selEnd = 0
                setSelection(selStart, selEnd)
                selectionChanged = true
            } else {
                //check to see if the current selection is outside the already entered text
                if (selStart > rawText.length() - 1) {
                    val start = fixSelection(selStart)
                    val end = fixSelection(selEnd)
                    if (start >= 0 && end < text!!.length) {
                        setSelection(start, end)
                    }
                }
            }
        }
        super.onSelectionChanged(selStart, selEnd)
    }

    private fun fixSelection(selection: Int): Int {
        return if (selection > lastValidPosition()) {
            lastValidPosition()
        } else {
            nextValidPosition(selection)
        }
    }

    private fun nextValidPosition(currentPosition: Int): Int {
        var position = currentPosition
        while (position < lastValidMaskPosition && maskToRaw[position] == -1) {
            position++
        }
        return if (position > lastValidMaskPosition) lastValidMaskPosition + 1 else position
    }

    private fun previousValidPosition(currentPosition: Int): Int {
        var position = currentPosition
        while (position >= 0 && maskToRaw[position] == -1) {
            position--
            if (position < 0) {
                return nextValidPosition(0)
            }
        }
        return position
    }

    private fun lastValidPosition(): Int {
        return if (rawText.length() == maxRawLength) {
            rawToMask[rawText.length() - 1] + 1
        } else nextValidPosition(rawToMask[rawText.length()])
    }

    private fun makeMaskedText(): String {
        val maskedTextLength: Int = if (rawText.length() < rawToMask.size) {
            rawToMask[rawText.length()]
        } else {
            mask!!.length
        }
        val maskedText =
            CharArray(maskedTextLength) //mask.replace(charRepresentation, ' ').toCharArray();
        for (i in maskedText.indices) {
            val rawIndex = maskToRaw[i]
            if (rawIndex == -1) {
                maskedText[i] = mask!![i]
            } else {
                maskedText[i] = rawText.charAt(rawIndex)
            }
        }
        return String(maskedText)
    }

    private fun makeMaskedTextWithHint(): CharSequence {
        val ssb = SpannableStringBuilder()
        var mtrv: Int
        val maskFirstChunkEnd = rawToMask[0]
        for (i in 0 until mask!!.length) {
            mtrv = maskToRaw[i]
            if (mtrv != -1) {
                if (mtrv < rawText.length()) {
                    ssb.append(rawText.charAt(mtrv))
                } else {
                    ssb.append(hint?.get(maskToRaw[i]) ?: charRepresentation)
                }
            } else {
                ssb.append(mask!![i])
            }
            if (keepHint && rawText.length() < rawToMask.size && i >= rawToMask[rawText.length()]
                || !keepHint && i >= maskFirstChunkEnd
            ) {
                ssb.setSpan(ForegroundColorSpan(currentHintTextColor), i, i + 1, 0)
            }
        }
        return ssb
    }

    private fun calculateRange(start: Int, end: Int): Range {
        val range = Range()
        var i = start
        while (i <= end && i < mask!!.length) {
            if (maskToRaw[i] != -1) {
                if (range.start == -1) {
                    range.start = maskToRaw[i]
                }
                range.end = maskToRaw[i]
            }
            i++
        }
        if (end == mask!!.length) {
            range.end = rawText.length()
        }
        if (range.start == range.end && start < end) {
            val newStart = previousValidPosition(range.start - 1)
            if (newStart < range.start) {
                range.start = newStart
            }
        }
        return range
    }

    private fun clear(s: String): String {
        var string = s

        if (deniedChars != null) {
            for (c in deniedChars!!.toCharArray()) {
                string = string.replace(c.toString(), "")
            }
        }

        if (allowedChars != null) {
            val builder = StringBuilder(string.length)
            for (c in string.toCharArray()) {
                if (allowedChars!!.contains(c.toString())) {
                    builder.append(c)
                }
            }
            string = builder.toString()
        }
        return string
    }

    companion object {
        const val SPACE = " "
    }

    class Range internal constructor() {
        var start: Int = -1
        var end: Int = -1
    }
}