package ru.github.igla.solareclipse

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View


class SolarEclipseView : View {

    var solarColor: Int = 0
    var startBgColor: Int = 0
    var endBgColor: Int = 0
    var pbPrimaryColor: Int = 0
    var pbSecondaryColor: Int = 0
    var eclipseSize: Int = -1

    private val solarColorDefault = context.getColorRes(R.color.sev_eclipse_color)
    private val startBgColorDefault = context.getColorRes(R.color.sev_start_bg_color)
    private val endBgColorDefault = context.getColorRes(R.color.sev_end_bg_color)
    private val pbPrimaryColorDefault = context.getColorRes(R.color.sev_primary_progress_color)
    private val pbSecondaryColorDefault = context.getColorRes(R.color.sev_secondary_progress_color)

    private lateinit var solarEclipseDrawable: SolarEclipseDrawable

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, attributeSetId: Int) : super(context, attrs, attributeSetId) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet? = null) {
        if (!isInEditMode) {
            if (attrs != null) {
                context.obtainStyledAttributes(attrs, R.styleable.SolarEclipseView)?.apply {
                    solarColor = getColor(R.styleable.SolarEclipseView_sev_solarColor, solarColorDefault)
                    startBgColor = getColor(R.styleable.SolarEclipseView_sev_bgStartColor, startBgColorDefault)
                    endBgColor = getColor(R.styleable.SolarEclipseView_sev_bgEndColor, endBgColorDefault)
                    pbPrimaryColor = getColor(R.styleable.SolarEclipseView_sev_pbPrimaryColor, pbPrimaryColorDefault)
                    pbSecondaryColor = getColor(R.styleable.SolarEclipseView_sev_pbSecondaryColor, pbSecondaryColorDefault)
                    eclipseSize = getDimensionPixelSize(R.styleable.SolarEclipseView_sev_eclipse_size, -1)
                    recycle()
                }
            }
            solarEclipseDrawable = SolarEclipseDrawable(context).apply {
                callback = this@SolarEclipseView
            }
            setDrawable(solarEclipseDrawable)
        }
    }

    @Suppress("DEPRECATION")
    private fun setDrawable(drawable: Drawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(drawable)
        } else {
            background = drawable
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        solarEclipseDrawable.startAnimation(ViewConfig(solarColor, startBgColor, endBgColor, pbPrimaryColor, pbSecondaryColor, eclipseSize))
    }

    override fun onDetachedFromWindow() {
        solarEclipseDrawable.stopAnimation()
        super.onDetachedFromWindow()
    }
}