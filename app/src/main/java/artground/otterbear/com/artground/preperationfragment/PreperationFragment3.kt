package artground.otterbear.com.artground.preperationfragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import artground.otterbear.com.artground.R
import artground.otterbear.com.artground.main.MainActivity
import kotlinx.android.synthetic.main.fragment_preperation_fragment3.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PreperationFragment3.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PreperationFragment3.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PreperationFragment3 : Fragment()  {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            preperation_btn1.setOnClickListener {

                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preperation_fragment3, container, false)
    }
}
