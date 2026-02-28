package com.example.android2m_sher_e_panjab.BottomNavigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.android2m_sher_e_panjab.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var spinner: Spinner

    private lateinit var areaTypeSpinner: Spinner

    private lateinit var propertyTypeSpinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        location spinner
        spinner = view.findViewById<Spinner>(R.id.locationSpinner)
        val list = arrayListOf<String>("Amritsar","Barnala","Bathinda","Faridkot","Fatehgarh Sahib","Fazilka","Ferozpur","Gurdaspur","Hoshiarpur","Jalandhar","Kapurthala","Ludhiana","Malerkotla","Mansa","Moga","Pathankot","Patiala","Rupnagar","Mohali","Sangrur","Nawanshahr","Sri Muktsar Sahib","Tarn Taran")
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long
            ) {

                val selectedItem = parent?.getItemAtPosition(position)

                Toast.makeText(requireActivity(), "${selectedItem.toString()}", Toast.LENGTH_SHORT).show()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

//        Property Type
        propertyTypeSpinner = view.findViewById<Spinner>(R.id.propertySpinner)
        val propertyTypeList = arrayListOf<String>("Agricultural","Commercial","Residential","Rented")
        val propertyAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, propertyTypeList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        propertyTypeSpinner.adapter = propertyAdapter
        propertyTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long
            ) {

                val selectedItem = parent?.getItemAtPosition(position)

                Toast.makeText(requireActivity(), "${selectedItem.toString()}", Toast.LENGTH_SHORT).show()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

//        Area Type
        areaTypeSpinner = view.findViewById<Spinner>(R.id.AreaSpinner)
        val areaTypeList = arrayListOf<String>("Marla","Killa","SqFt","BHK")
        val areaAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, areaTypeList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        areaTypeSpinner.adapter = areaAdapter
        areaTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long
            ) {

                val selectedItem = parent?.getItemAtPosition(position)

                Toast.makeText(requireActivity(), "${selectedItem.toString()}", Toast.LENGTH_SHORT).show()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}