package com.zch.base.base

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.zch.utils.KeyBoardUtils
import com.zch.base.R
import com.zch.base.app.CommonUtil
import com.zch.base.event.ErrorEvent
import com.zch.base.event.Message
import com.zch.base.listener.OnLoadingViewListener
import com.zch.base.util.PadUtils
import com.zch.base.util.StatusBarUtils
import com.zch.base.util.ViewUtils
import com.zch.base.widget.CommonToolBar
import com.zch.base.widget.LoadingDialog
import com.zch.utils.ToastUtils
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 * 通用Base组件
 *
 * Description: Activity基类
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
abstract class BaseActivity<VM : BaseViewModel, DB : ViewBinding> : AppCompatActivity(),
    OnLoadingViewListener {

    protected lateinit var mViewModel: VM

    protected lateinit var mBinding: DB

    private  var dialog: LoadingDialog?=null

    private lateinit var toolbar: CommonToolBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewDataBinding()
        initStatusBar(statusBarColorId())
        initToolBar()
        lifecycle.addObserver(mViewModel)
        registorDefUIChange()
        readIntent()
        initView(savedInstanceState)
        if (PadUtils.isPad(this)) {
            adaptForPad(mBinding)
        }
        initData()
    }

    open fun layoutId(): Int = 0
    abstract fun statusBarColorId(): Int
    abstract fun isActivityDarkMode(): Boolean
    open fun readIntent(){}
    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun initData()
    open fun adaptForPad(binding: DB){}

    private fun initToolBar() {
        if (findViewById<View?>(R.id.toolbar) != null) {
            toolbar = findViewById<View>(R.id.toolbar) as CommonToolBar
            if (!TextUtils.isEmpty(title)) {
                toolbar.setTitle(title.toString())
            }
        }
    }

    /**
     * 参数为0时，背景图片会顶到状态栏中，体现为背景图片的沉浸式效果。
     * 参数不为0时，会根据ColorId设置状态栏背景。
     */
    protected fun initStatusBar(colorId : Int) {
        if (statusBarColorId() == 0) {
            setStatusTrans()
        } else {
            StatusBarUtils.setColor(this, colorId)
        }
        if (!isActivityDarkMode()) {
            ViewUtils.setStatusBarDarkMode(true,this)
        }
    }


    /**
     * 状态栏透明：适合选择图片做为背景时的选择
     */
    private fun setStatusTrans() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.statusBarColor = Color.TRANSPARENT
    }


    /**
     * DataBinding or ViewBinding
     */
    private fun initViewDataBinding() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val cls = type.actualTypeArguments[1] as Class<*>
            when {
                ViewDataBinding::class.java.isAssignableFrom(cls) && cls != ViewDataBinding::class.java -> {
                    if (layoutId() == 0) throw IllegalArgumentException("Using DataBinding requires overriding method layoutId")
                    mBinding = DataBindingUtil.setContentView(this, layoutId())
                    (mBinding as ViewDataBinding).lifecycleOwner = this
                }
                ViewBinding::class.java.isAssignableFrom(cls) && cls != ViewBinding::class.java -> {
                    cls.getDeclaredMethod("inflate", LayoutInflater::class.java).let {
                        @Suppress("UNCHECKED_CAST")
                        mBinding = it.invoke(null, layoutInflater) as DB
                        setContentView(mBinding.root)
                    }
                }
                else -> {
                    if (layoutId() == 0) throw IllegalArgumentException("If you don't use ViewBinding, you need to override method layoutId")
                    setContentView(layoutId())
                }
            }
            createViewModel(type.actualTypeArguments[0])
        } else throw IllegalArgumentException("Generic error")
    }


    /**
     * 注册 UI 事件
     */
    private fun registorDefUIChange() {
        mViewModel.defUI.showDialog.observe(this) {
            onShowLoadingDialog()
        }
        mViewModel.defUI.dismissDialog.observe(this) {
            onDismissLoadingDialog()
        }
        mViewModel.defUI.toastEvent.observe(this) {
            ToastUtils.showToast(this, it)
        }
        mViewModel.defUI.msgEvent.observe(this) {
            handleEvent(it)
        }
        mViewModel.defUI.showNetError.observe(this) {
            showNetError(it)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    /**
     * 网络错误处理
     */
    open fun showNetError(event: ErrorEvent) {

    }

    open fun handleEvent(msg: Message) {}


    override fun onShowLoadingDialog() {
        if (!isFinishing) {
            dialog = LoadingDialog(this)
            dialog?.show()
        }
    }

    override fun onShowLoadingDialog(loadingText: String) {
        if (!isFinishing) {
            dialog = LoadingDialog(this)
            dialog?.setLoadingText(loadingText)
            dialog?.show()
        }
    }

    override fun onDismissLoadingDialog() {
        dialog?.run { if (isShowing) dismiss() }
    }


    /**
     * 创建 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewModel(type: Type) {
        val tClass = type as? Class<VM> ?: BaseViewModel::class.java
        mViewModel = ViewModelProvider(viewModelStore, defaultViewModelProviderFactory)
            .get(tClass) as VM
    }

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return CommonUtil.getConfig().viewModelFactory()
            ?: super.getDefaultViewModelProviderFactory()
    }

    open override fun onDestroy() {
        super.onDestroy()

    }

    /**
     * 清除editText的焦点
     *
     * @param v   焦点所在View
     * @param ids 输入框
     */
    open fun clearViewFocus(v: View?, vararg ids: Int) {
        if (null != v && null != ids && ids.size > 0) {
            for (id in ids) {
                if (v.id == id) {
                    v.clearFocus()
                    break
                }
            }
        }
    }

    /**
     * 隐藏键盘
     *
     * @param v   焦点所在View
     * @param ids 输入框
     * @return true代表焦点在edit上
     */
    open fun isFocusEditText(v: View?, vararg ids: Int): Boolean {
        if (v is EditText) {
            for (id in ids) {
                if (v.id == id) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 是否触摸在指定view上面,对某个控件过滤
     *
     * @param views
     * @param ev
     * @return
     */
    open fun isTouchView(views: Array<View>?, ev: MotionEvent): Boolean {
        if (views == null || views.size == 0) {
            return false
        }
        val location = IntArray(2)
        for (view in views) {
            view.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]
            if (ev.x > x && ev.x < x + view.width && ev.y > y && ev.y < y + view.height) {
                return true
            }
        }
        return false
    }

    /**
     * 是否触摸在指定view上面,对某个控件过滤
     *
     * @param ids
     * @param ev
     * @return
     */
    open fun isTouchView(ids: IntArray?, ev: MotionEvent): Boolean {
        val location = IntArray(2)
        for (id in ids!!) {
            val view = findViewById<View>(id) ?: continue
            view.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]
            if (ev.x > x && ev.x < x + view.width && ev.y > y && ev.y < y + view.height) {
                return true
            }
        }
        return false
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            if (isTouchView(filterViewByIds(), ev)) {
                return super.dispatchTouchEvent(ev)
            }
            if (hideSoftByEditViewIds() == null || hideSoftByEditViewIds()!!.size == 0) {
                return super.dispatchTouchEvent(ev)
            }
            val v = currentFocus
            if (isFocusEditText(v, *hideSoftByEditViewIds()!!)) {
                if (isTouchView(hideSoftByEditViewIds(), ev)) {
                    return super.dispatchTouchEvent(ev)
                }
                KeyBoardUtils.hideInputForce(this)
                clearViewFocus(v, *hideSoftByEditViewIds()!!)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 传入EditText的Id
     * 没有传入的EditText不做处理
     *
     * @return id 数组
     */
    open fun hideSoftByEditViewIds(): IntArray? {
        return null
    }

    /**
     * 传入要过滤的View
     * 过滤之后点击将不会有隐藏软键盘的操作
     *
     * @return id 数组
     */
    open fun filterViewByIds(): Array<View>? {
        return null
    }

    override fun getResources(): Resources {
        val resources: Resources = super.getResources()
        val configuration: Configuration = resources.configuration
        //判断是否为pad
        if ((configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                    >= Configuration.SCREENLAYOUT_SIZE_LARGE) && configuration.fontScale == 1f) {
            configuration.fontScale = 1.12f
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
        return resources
    }

}
