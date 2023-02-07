package int20h.troipsa.pseudocalendar.ui.main

import dagger.hilt.android.lifecycle.HiltViewModel
import int20h.troipsa.pseudocalendar.data.prefs.PseudoPrefsManager
import int20h.troipsa.pseudocalendar.data.prefs.base.get
import int20h.troipsa.pseudocalendar.data.prefs.base.set
import int20h.troipsa.pseudocalendar.domain.interactors.*
import int20h.troipsa.pseudocalendar.domain.models.Event
import int20h.troipsa.pseudocalendar.domain.models.File
import int20h.troipsa.pseudocalendar.ui.base.view_model.BaseViewModel
import int20h.troipsa.pseudocalendar.utils.coroutines.launchAndCollect
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getUserFlowInteractor: GetUserFlowInteractor,
    private val addEventInteractor: AddEventInteractor,
    private val prefsManager: PseudoPrefsManager,
    private val getEventFilesInteractor: GetEventFilesInteractor,
    private val deleteEventFilesInteractor: DeleteEventFilesInteractor,
    private val deleteFilesInteractor: DeleteFilesInteractor,
    private val addEventFilesInteractor: AddEventFilesInteractor,
    private val addFilesInteractor: AddFilesInteractor,
    private val getFileInteractor: GetFileInteractor,
) : BaseViewModel() {

    val user = getUserFlowInteractor()
        .stateIn(scope = scope, SharingStarted.Eagerly, null)

    private val _eventId = MutableStateFlow(0)

    private val _eventFiles = MutableStateFlow<List<File>>(emptyList())

    private val _attachedEventFiles = MutableStateFlow<List<File>>(emptyList())
    val attachedEventFiles = _attachedEventFiles.asStateFlow()

    val canSave = _attachedEventFiles.map {
        it != _eventFiles.value && _eventId.value != 0
    }.stateIn(scope = scope, SharingStarted.Eagerly, false)

    fun generateEvents() {
        runCoroutine {
            if (!prefsManager.isEventsGenerated.get()) {
                addEventInteractor(Event.generateEvents())
                prefsManager.isEventsGenerated.set(true)
            }
        }
    }

    fun setFiles(files: List<String>) {
        runCoroutine {
            files.forEach { addFilesInteractor(it) }
            val filesList = files.map { getFileInteractor(it) }
            filesList.forEach { file ->
                addEventFilesInteractor(_eventId.value, file.id)
            }
            getEventFilesInteractor(_eventId.value).launchAndCollect(scope) {
                _eventFiles.value = it
                _attachedEventFiles.value = it
            }
        }
    }

    fun setEventId(eventId: Int) {
        _eventId.value = eventId
    }

    fun clearFiles() {
        runCoroutine {
            deleteEventFilesInteractor(_eventId.value)
            _attachedEventFiles.value.map { deleteFilesInteractor(it.name) }
        }
    }

}