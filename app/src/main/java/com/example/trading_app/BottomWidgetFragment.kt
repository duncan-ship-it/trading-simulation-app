package com.example.trading_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar


class BottomWidgetFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_widget_fragment, container, false)

        view.setOnClickListener {
            activity?.let { a ->

                // confirm the user wants to exit the app
                val snack = Snackbar.make(
                    view,
                    resources.getString(R.string.confirm_exit),
                    Snackbar.LENGTH_LONG
                )
                snack.setAction(resources.getString(R.string.yes)) {
                    a.finish()
                }

                snack.show()

            }
        }

        return view
    }
}