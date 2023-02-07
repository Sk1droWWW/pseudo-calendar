package int20h.troipsa.pseudocalendar.ui.contacts

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import int20h.troipsa.pseudocalendar.domain.models.Contact
import int20h.troipsa.pseudocalendar.ui.base.view_model.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class ContactsVievModel : BaseViewModel() {

    private val _contactList = MutableStateFlow(emptyList<Contact>())
    val contactList = _contactList.asStateFlow()

    fun addContact(contact: Contact) {
        _contactList.value = _contactList.value.plusElement(contact)
    }

    fun hasContactPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED;
    }

    fun requestContactPermission(context: Context, activity: Activity) {
        if (!hasContactPermission(context)) {
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.READ_CONTACTS), 1)
        }
    }

    fun Context.findActivity(): Activity {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        throw IllegalStateException("no activity")
    }

    fun getContactList(
        context: Context
    ) {
        _contactList.value = getNamePhoneDetails(context)!!
    }

    @SuppressLint("Range")
    fun getNamePhoneDetails(
        context: Context
    ): MutableList<Contact>? {
        val names = mutableListOf<Contact>()
        val cr = context.contentResolver
        val cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            null, null, null)
        if (cur!!.count > 0) {
            while (cur.moveToNext()) {
                val id = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID)).toInt()
                val name = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                names.add(Contact(id , name , number))
            }
        }
        return names
    }
}