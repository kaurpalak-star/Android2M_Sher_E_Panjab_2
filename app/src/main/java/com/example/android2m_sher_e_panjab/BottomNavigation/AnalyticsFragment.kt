package com.example.android2m_sher_e_panjab.BottomNavigation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.android2m_sher_e_panjab.databinding.FragmentAnalyticsBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.*

class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!

    private val database = FirebaseDatabase.getInstance().getReference("land_analytics")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchAnalyticsData()

//        uploadLandAnalytics()
    }

    private fun fetchAnalyticsData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) return

                // 1. Sector Distribution (Pie Chart)
                val sectorSnapshot = snapshot.child("sector_distribution")
                setupPieChart(sectorSnapshot)

                // 2. Price Trends (Line Chart)
                val priceTrendsSnapshot = snapshot.child("price_trends")
                setupLineChart(priceTrendsSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


    private fun uploadLandAnalytics() {
        val database = FirebaseDatabase.getInstance().getReference("land_analytics")

        // 1. Create the Summary Map
        val summary = mapOf(
            "total_listings" to "4,850",
            "avg_price" to "₹52 Lakh/Acre",
            "hotspot" to "Ludhiana"
        )

        // 2. Create the Sector Distribution Map
        val sectorDistribution = mapOf(
            "Agricultural" to 55,
            "Residential" to 25,
            "Commercial" to 15,
            "Industrial" to 5
        )

        // 3. Create Price Trends (Lists are handled as indexed keys in Firebase)
        val priceTrends = mapOf(
            "labels" to listOf("2022", "2023", "2024", "2025", "2026"),
            "prices" to listOf(38, 42, 45, 49, 52)
        )

        // 4. Create District Data
        val districtData = mapOf(
            "d1" to mapOf("name" to "Ludhiana", "growth" to 8.4, "avg" to 65),
            "d2" to mapOf("name" to "Amritsar", "growth" to 6.2, "avg" to 58),
            "d3" to mapOf("name" to "Jalandhar", "growth" to 7.1, "avg" to 60),
            "d4" to mapOf("name" to "Mohali", "growth" to 12.5, "avg" to 85),
            "d5" to mapOf("name" to "Patiala", "growth" to 4.8, "avg" to 45)
        )

        // Combine all into one master map
        val masterData = mapOf(
            "summary" to summary,
            "sector_distribution" to sectorDistribution,
            "price_trends" to priceTrends,
            "district_data" to districtData
        )

        // Upload to Firebase
        database.setValue(masterData).addOnSuccessListener {
            Toast.makeText(context, "Analytics Data Updated!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setupPieChart(data: DataSnapshot) {
        val entries = ArrayList<PieEntry>()

        // Loop through Firebase children (Agricultural, Residential, etc.)
        for (child in data.children) {
            val label = child.key ?: "Unknown"
            val value = child.value.toString().toFloatOrNull() ?: 0f
            entries.add(PieEntry(value, label))
        }

        val dataSet = PieDataSet(entries, "Land Use %")

        // Styling the Pie Chart
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 14f

        val pieData = PieData(dataSet)
        binding.pieChart.apply {
            this.data = pieData
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            setEntryLabelColor(Color.BLACK)
            animateY(1400)
            invalidate() // Refresh chart
        }
    }

    private fun setupLineChart(data: DataSnapshot) {
        val labels = ArrayList<String>()
        val entries = ArrayList<Entry>()

        // Get Labels (Years: 2022, 2023...)
        val labelsData = data.child("labels")
        labelsData.children.forEach { labels.add(it.value.toString()) }

        // Get Prices
        val pricesData = data.child("prices")
        var index = 0f
        pricesData.children.forEach {
            val price = it.value.toString().toFloatOrNull() ?: 0f
            entries.add(Entry(index, price))
            index++
        }

        val lineDataSet = LineDataSet(entries, "Avg Price (Lakhs/Acre)")

        // Styling the Line Chart
        lineDataSet.color = Color.parseColor("#4CAF50") // Green matching Punjab theme
        lineDataSet.setCircleColor(Color.BLACK)
        lineDataSet.lineWidth = 3f
        lineDataSet.circleRadius = 5f
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = Color.parseColor("#4CAF50")
        lineDataSet.fillAlpha = 50
        lineDataSet.valueTextSize = 12f

        val lineData = LineData(lineDataSet)
        binding.lineChart.apply {
            this.data = lineData
            description.isEnabled = false

            // X-Axis Setup
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)

            // Y-Axis Setup
            axisRight.isEnabled = false
            axisLeft.setDrawGridLines(true)

            animateX(1000)
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}