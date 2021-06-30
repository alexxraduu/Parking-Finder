package com.parkingfinder.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.parkingfinder.R
import com.parkingfinder.interfaces.ActivityFragmentCommunication

class DevelopersInfo(private val info: String) : Fragment() {
    private var activityFragmentCommunication: ActivityFragmentCommunication? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_developers_info, container, false)

        val tvInfo: TextView = view.findViewById(R.id.tv_info)
        tvInfo.text = info

        val toolbar: Toolbar = view.findViewById(R.id.toolbar_back_dev)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActivityFragmentCommunication) {
            activityFragmentCommunication = context
        }
    }


}
