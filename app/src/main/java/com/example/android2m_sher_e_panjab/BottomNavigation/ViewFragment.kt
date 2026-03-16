package com.example.android2m_sher_e_panjab.BottomNavigation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.android2m_sher_e_panjab.ImageSliderAdapter
import com.example.android2m_sher_e_panjab.Property
import com.example.android2m_sher_e_panjab.R
import com.example.android2m_sher_e_panjab.databinding.FragmentViewBinding

class ViewFragment : Fragment(R.layout.fragment_view) {
    private var _binding: FragmentViewBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentViewBinding.bind(view)

        val property = arguments?.getSerializable("property_data") as? Property
        property?.let { data ->
            binding.viewTitle.text = data.name
            binding.viewPrice.text = data.price
            binding.viewLocation.text = data.location
            binding.viewArea.text = "Area: ${data.area} ${data.areaUnit}"
            binding.viewType.text = "Type: ${data.propertyType}"
            binding.viewSoil.text = "Soil: ${data.soilType}"
            binding.viewDescription.text = data.description
            binding.viewContact.text = "Contact: ${data.contact}"

            val adapter = ImageSliderAdapter(data.imageUrls)
            binding.viewPagerImages.adapter = adapter
            binding.tvImageCount.text = "1/${data.imageUrls.size}"

            binding.viewPagerImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.tvImageCount.text = "${position + 1}/${data.imageUrls.size}"
                }
            })

            binding.btnCall.setOnClickListener {
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${data.contact}")))
            }
        }
    }
}