package com.example.todousingcompose.repository

import com.example.todousingcompose.model.FirestoreResponse
import com.example.todousingcompose.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class HomeRepositoryImplementation @Inject constructor(
    private val firestoreDb: FirebaseFirestore
) : HomeRepository {

    override fun insert(item: FirestoreResponse): Flow<Resource<String>> = callbackFlow{
        trySend(Resource.Loading)
        firestoreDb.collection("user")
            .add(item)
            .addOnSuccessListener {
                trySend(Resource.Success("Data is inserted with ${it.id}"))
            }.addOnFailureListener {
                trySend(Resource.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun getItems(): Flow<Resource<List<FirestoreResponse>>> =  callbackFlow{
        trySend(Resource.Loading)
        firestoreDb.collection("user")
            .get()
            .addOnSuccessListener {
                val items =  it.map { data->
                    FirestoreResponse(
                        task = data["task"] as String?,
                        key = data.id
                    )
                }
                trySend(Resource.Success(items))
            }.addOnFailureListener {
                trySend(Resource.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun delete(key: String): Flow<Resource<String>> = callbackFlow{
        trySend(Resource.Loading)
        firestoreDb.collection("user")
            .document(key)
            .delete()
            .addOnCompleteListener {
                if(it.isSuccessful)
                    trySend(Resource.Success("Deleted successfully.."))
            }.addOnFailureListener {
                trySend(Resource.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun update(item: FirestoreResponse): Flow<Resource<String>> = callbackFlow{
        trySend(Resource.Loading)
        val map = HashMap<String,Any>()
        map["task"] = item.task!!

        firestoreDb.collection("user")
            .document(item.key!!)
            .update(map)
            .addOnCompleteListener {
                if(it.isSuccessful)
                    trySend(Resource.Success("Update successfully..."))
            }.addOnFailureListener {
                trySend(Resource.Failure(it))
            }
        awaitClose {
            close()
        }
    }
}