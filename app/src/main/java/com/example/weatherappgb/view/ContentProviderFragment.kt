package com.example.weatherappgb.view

import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.weatherappgb.R
import com.example.weatherappgb.databinding.FragmentContentProviderBinding


const val REQUEST_CODE = 42

class ContentProviderFragment : Fragment() {

    private var _binding: FragmentContentProviderBinding? = null
    private val binding: FragmentContentProviderBinding
        get() {
            return _binding!!
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getContacts()
        }else if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_CONTACTS)){
            explain()
        }else{
            mRequestPermission()
        }
    }
    private fun explain(){
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.alert_dialogue_title))
            .setMessage(resources.getString(R.string.alert_dialogue_message))
            .setPositiveButton(resources.getString(R.string.permission_positive)) { _, _ ->
                mRequestPermission()
            }
            .setNegativeButton(resources.getString(R.string.permission_negative)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
    private fun mRequestPermission() {
        requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) { if (requestCode == REQUEST_CODE) {
        for (i in permissions.indices) {
            if (permissions[i] == android.Manifest.permission.READ_CONTACTS && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                getContacts()
            } else {
                explain()
            }
        }
    } else {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    }
    private fun getContacts(){
        val contentResolver: ContentResolver = requireContext().contentResolver
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )
        cursor?.let {
            for (i in 0 until it.count){
                if(cursor.moveToPosition(i)){
                    val columnNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    val name:String = cursor.getString(columnNameIndex)
                    binding.containerForContacts.addView(TextView(requireContext()).apply {
                        textSize = 30f
                        text= name
                    })
                }
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = ContentProviderFragment()
     }
}
