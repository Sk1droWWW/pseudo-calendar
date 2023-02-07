package int20h.troipsa.pseudocalendar.ui.contacts

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import int20h.troipsa.pseudocalendar.domain.interactors.AddContactInteractor
import int20h.troipsa.pseudocalendar.domain.interactors.GetContactListInteractor
import int20h.troipsa.pseudocalendar.domain.models.Contact
import int20h.troipsa.pseudocalendar.ui.base.view_model.BaseViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class ContactsVievModel @Inject constructor(
    private val addContactInteractor: AddContactInteractor,
    private val getContactListInteractor: GetContactListInteractor,
) : BaseViewModel() {

    val contactList = getContactListInteractor()
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    fun addContact(uri: Uri?, context: Context) {
        runCoroutine(
            withProgress = true,
        ) {
            if (uri != null) {
                val cursor = context.contentResolver.query(
                    uri,
                    null,
                    null,
                    null,
                    null
                ) ?: return@runCoroutine

                if (cursor.moveToFirst()) {
                    val columnIdIndex = cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID)
                    val columnNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

                    if (columnNameIndex != -1 && columnIdIndex != -1) {
                        val id = cursor.getString(columnIdIndex).toInt()
                        val name = cursor.getString(columnNameIndex)

                        Log.i("PSEUDO_APP", "addContact: $id $name")

                        addContactInteractor(Contact(id, name))
                    }
                    cursor.close()
                }
            }
        }
    }
}