package com.maisel.contacts

sealed class ContactsDestination {
    data class ChatDetail(val contactId: String) : ContactsDestination()
}
