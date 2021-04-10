package dev.efemoney.lexiko

import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.cloud.FirestoreClient
import dagger.Module
import dagger.Provides

@Module
interface FirebaseModule {

  companion object {

    @Provides
    @ApplicationScope
    fun app(): FirebaseApp = FirebaseApp.initializeApp()

    @Provides
    @ApplicationScope
    fun auth(app: FirebaseApp): FirebaseAuth = FirebaseAuth.getInstance(app)

    @Provides
    @ApplicationScope
    fun firestore(app: FirebaseApp): Firestore = FirestoreClient.getFirestore(app)
  }
}
