import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.navercorp.nid.NidOAuth

@Composable
fun rememberNaverLoginManager(
    onResult: (String?) -> Unit
): () -> Unit {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->

        if (result.resultCode == Activity.RESULT_CANCELED) {
            Log.d("Siria22 - NaverLoginManager", "User cancelled the login flow.")
            onResult(null)
            return@rememberLauncherForActivityResult
        }

        if (result.resultCode != Activity.RESULT_OK) {
            Log.w("Siria22 - NaverLoginManager", "Login failed with resultCode: ${result.resultCode}")
            onResult(null)
            return@rememberLauncherForActivityResult
        }
        val lastError = NidOAuth.getLastErrorCode()
        val errorCode = lastError.code
        val errorDescription = NidOAuth.getLastErrorDescription()
        Log.e("Siria22 - NaverLoginManager", "errorCode:$errorCode, errorDesc:$errorDescription")
        onResult(null)
    }

    return remember(context, launcher) {
        {
            NidOAuth.requestLogin(context, launcher)
        }
    }
}