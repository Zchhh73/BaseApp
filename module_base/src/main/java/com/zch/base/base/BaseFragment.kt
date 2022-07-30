package com.zch.base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.zch.base.app.CommonUtil
import com.zch.base.event.ErrorEvent
import com.zch.base.event.Message
import com.zch.base.listener.OnLoadingViewListener
import com.zch.base.util.PadUtils
import com.zch.base.widget.LoadingDialog
import com.zch.utils.ToastUtils
import java.lang.reflect.ParameterizedType

/**
 * 通用Base组件
 *
 * Description: fragmnet基类
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
abstract class BaseFragment<VM : BaseViewModel, DB : ViewBinding> : Fragment(),
    OnLoadingViewListener {

    protected lateinit var mViewModel: VM

    private var _binding: DB? = null
    protected val mBinding get() = _binding!!

    //是否第一次加载
    private var isFirst: Boolean = true

    private var dialog: LoadingDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return initBinding(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readIntent()
        onVisible()
        createViewModel()
        lifecycle.addObserver(mViewModel)
        //注册 UI事件
        registorDefUIChange()
        initView(savedInstanceState)
        if (PadUtils.isPad(requireContext())) {
            adaptForPad(mBinding)
        }
        initData()
        setListener()
    }

    open fun readIntent() {}
    open fun initData() {}
    open fun initView(savedInstanceState: Bundle?) {}
    open fun setListener() {}
    open fun adaptForPad(binding: DB){}
    override fun onResume() {
        super.onResume()
        onVisible()
    }

    /**
     * 使用 DataBinding时,要重写此方法返回相应的布局 id
     * 使用ViewBinding时，不用重写此方法
     */
    open fun layoutId(): Int = 0

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()
            isFirst = false
        }
    }

    /**
     * 懒加载
     */
    open fun lazyLoadData() {}

    /**
     * 注册 UI 事件
     */
    private fun registorDefUIChange() {
        mViewModel.defUI.showDialog.observe(viewLifecycleOwner) {
            onShowLoadingDialog()
        }
        mViewModel.defUI.dismissDialog.observe(viewLifecycleOwner) {
            onDismissLoadingDialog()
        }
        mViewModel.defUI.toastEvent.observe(viewLifecycleOwner) {
            ToastUtils.showToast(requireContext(), it)
        }
        mViewModel.defUI.msgEvent.observe(viewLifecycleOwner) {
            handleEvent(it)
        }
        mViewModel.defUI.showNetError.observe(this) {
            showNetError(it)
        }
    }

    /**
     * 网络错误处理
     */
    open fun showNetError(event: ErrorEvent) {

    }


    open fun handleEvent(msg: Message) {}

    override fun onShowLoadingDialog() {
        dialog = LoadingDialog(activity)
        dialog?.show()

    }

    override fun onShowLoadingDialog(loadingText: String) {
        dialog = LoadingDialog(activity)
        dialog?.setLoadingText(loadingText)
        dialog?.show()

    }

    override fun onDismissLoadingDialog() {
        dialog?.run { if (isShowing) dismiss() }
    }


    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val cls = type.actualTypeArguments[1] as Class<*>
            return when {
                ViewDataBinding::class.java.isAssignableFrom(cls) && cls != ViewDataBinding::class.java -> {
                    if (layoutId() == 0) throw IllegalArgumentException("Using DataBinding requires overriding method layoutId")
                    _binding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
                    (mBinding as ViewDataBinding).lifecycleOwner = this
                    mBinding.root
                }
                ViewBinding::class.java.isAssignableFrom(cls) && cls != ViewBinding::class.java -> {
                    cls.getDeclaredMethod("inflate", LayoutInflater::class.java).let {
                        @Suppress("UNCHECKED_CAST")
                        _binding = it.invoke(null, inflater) as DB
                        this.viewLifecycleOwner.lifecycle.addObserver(object :
                            DefaultLifecycleObserver {
                            override fun onDestroy(owner: LifecycleOwner) {
                                _binding = null
                            }
                        })
                        mBinding.root
                    }
                }
                else -> {
                    if (layoutId() == 0) throw IllegalArgumentException("If you don't use ViewBinding, you need to override method layoutId")
                    inflater.inflate(layoutId(), container, false)
                }
            }
        } else throw IllegalArgumentException("Generic error")
    }

    /**
     * 创建 ViewModel
     * 共享 ViewModel的时候，重写  Fragmnt 的 getViewModelStore() 方法，
     * 返回 activity 的  ViewModelStore 或者 父 Fragmnt 的 ViewModelStore
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            mViewModel = ViewModelProvider(viewModelStore, defaultViewModelProviderFactory)
                .get(tClass) as VM
        }
    }

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return CommonUtil.getConfig().viewModelFactory()
            ?: super.getDefaultViewModelProviderFactory()
    }

}
