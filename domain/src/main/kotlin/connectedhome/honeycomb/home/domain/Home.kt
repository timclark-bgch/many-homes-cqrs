package connectedhome.honeycomb.home.domain

data class Owner(val id: String)

sealed class Event
data class Created(val owner: Owner) : Event()
data class Migrated(val owner: Owner) : Event()
data class Suspended(val reason: String) : Event()
data class Closed(val reason: String) : Event()



