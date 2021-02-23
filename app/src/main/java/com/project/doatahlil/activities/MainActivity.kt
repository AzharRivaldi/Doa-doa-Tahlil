package com.project.doatahlil.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.doatahlil.R
import com.project.doatahlil.adapter.AdapterDoa
import com.project.doatahlil.model.ModelDoa
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

class MainActivity : AppCompatActivity() {

    var adapterDoa: AdapterDoa? = null
    var modelDoa: MutableList<ModelDoa> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }

        searchDoa.setImeOptions(EditorInfo.IME_ACTION_DONE)
        searchDoa.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                adapterDoa?.filter.filter(newText)
                return true
            }
        })

        //transparent background searchview
        val searchPlateId = searchDoa.getContext().resources.getIdentifier("android:id/search_plate",
                null, null)
        val searchPlate = searchDoa.findViewById<View>(searchPlateId)
        searchPlate?.setBackgroundColor(Color.TRANSPARENT)

        rvListDoa.setLayoutManager(LinearLayoutManager(this))
        rvListDoa.setHasFixedSize(true)

        fabSource.setOnClickListener(View.OnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://islam.nu.or.id/post/read/107344/susunan-bacaan-tahlil-doa-arwah-lengkap-dan-terjemahannya"))
            )
        })

        getDataDoa()
    }

    private fun getDataDoa() {
            try {
                val stream = assets.open("doatahlil.json")
                val size = stream.available()
                val buffer = ByteArray(size)
                stream.read(buffer)
                stream.close()
                val strResponse = String(buffer, StandardCharsets.UTF_8)
                try {
                    val jsonObject = JSONObject(strResponse)
                    val jsonArray = jsonObject.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        val jsonObjectData = jsonArray.getJSONObject(i)
                        val dataModel = ModelDoa()
                        dataModel.strId = jsonObjectData.getString("id")
                        dataModel.strTitle = jsonObjectData.getString("title")
                        dataModel.strArabic = jsonObjectData.getString("arabic")
                        dataModel.strTranslation = jsonObjectData.getString("translation")
                        modelDoa.add(dataModel)
                    }
                    adapterDoa = AdapterDoa(this, modelDoa)
                    rvListDoa.adapter = adapterDoa
                    adapterDoa?.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } catch (ignored: IOException) {
            }
        }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val window = activity.window
            val layoutParams = window.attributes
            if (on) {
                layoutParams.flags = layoutParams.flags or bits
            } else {
                layoutParams.flags = layoutParams.flags and bits.inv()
            }
            window.attributes = layoutParams
        }
    }

}