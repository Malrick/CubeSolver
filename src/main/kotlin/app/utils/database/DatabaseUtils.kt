package app.utils.database

import app.model.database.Database
import jetbrains.exodus.ByteIterable
import jetbrains.exodus.bindings.StringBinding
import jetbrains.exodus.env.*
import org.koin.core.KoinComponent

class DatabaseUtils : KoinComponent {

    private val env: Environment

    private val cornerStore : Store

    private val edgeStore : Store

    private val edgeStore2 : Store

    init {
        env = Environments.newInstance("db/data")

        cornerStore = env.computeInTransaction { txn -> env.openStore("CornerStore", StoreConfig.WITHOUT_DUPLICATES, txn) }

        edgeStore = env.computeInTransaction { txn -> env.openStore("EdgeStore", StoreConfig.WITHOUT_DUPLICATES, txn) }

        edgeStore2 = env.computeInTransaction { txn -> env.openStore("EdgeStore2", StoreConfig.WITHOUT_DUPLICATES, txn) }

    }

    fun addElement(database : Database, key : String, value : String)
    {
        var myKey: ByteIterable = StringBinding.stringToEntry(key)
        var myValue: ByteIterable = StringBinding.stringToEntry(value)

        when(database)
        {
            Database.CORNERS -> env.executeInTransaction { txn -> cornerStore.put(txn, myKey, myValue) }
            Database.EDGES -> env.executeInTransaction { txn -> edgeStore.put(txn, myKey, myValue) }
            Database.EDGES2 -> env.executeInTransaction { txn -> edgeStore2.put(txn, myKey, myValue) }
        }

    }

    fun queryElement(database: Database, key : String) : String?
    {
        var myKey: ByteIterable = StringBinding.stringToEntry(key)

        var result = when(database)
        {
            Database.CORNERS -> env.computeInReadonlyTransaction { txn -> cornerStore[txn, myKey] }
            Database.EDGES -> env.computeInReadonlyTransaction { txn -> edgeStore[txn, myKey] }
            Database.EDGES2 -> env.computeInReadonlyTransaction { txn -> edgeStore2[txn, myKey] }
        }

        return when(result)
        {
            null -> null
            else -> StringBinding.entryToString(result)
        }
    }


}