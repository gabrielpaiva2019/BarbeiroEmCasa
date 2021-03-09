package com.barbeiroemcasa.ui.loading

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.barbeiroemcasa.R
import kotlinx.android.synthetic.main.loading_fragment.*

class LoadingDialogFragment : DialogFragment() {

    private var mCallback: OnLoadingDialogCancelListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.loading_fragment, null)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    }

    override fun onDismiss(dialog: DialogInterface) {
        changeVisibilityActivity(View.VISIBLE)
        super.onDismiss(dialog)
    }

    override fun onDetach() {
        super.onDetach()
        changeVisibilityActivity(View.VISIBLE)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        mCallback?.onCancel()
    }

    interface OnLoadingDialogCancelListener {
        fun onCancel()
    }


    companion object {
        const val DEFAULT_TITLE = "Carregando\nAguarde ..."

        fun newInstance(
            fragmentManager: FragmentManager,
            callback: OnLoadingDialogCancelListener
        ): LoadingDialogFragment {

            val loadingDialogFragment = LoadingDialogFragment()
            loadingDialogFragment.mCallback = callback

            return loadingDialogFragment
        }
    }

    fun setCustomTitle(title: String?){
        textViewLoadingTitle?.text = title
    }


    private fun changeVisibilityActivity(visibility: Int) {
        activity?.window?.decorView?.findViewById<View>(android.R.id.content)?.visibility =
            visibility
    }
}