package com.fanhl.helloamazon

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.amazon.identity.auth.device.AuthError
import com.amazon.identity.auth.device.api.authorization.AuthCancellation
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult
import com.amazon.identity.auth.device.api.authorization.ProfileScope
import com.amazon.identity.auth.device.api.workflow.RequestContext


class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private var requestContext: RequestContext? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById<TextView>(R.id.textView)
        findViewById<Button>(R.id.btn_amazon).setOnClickListener {
            AuthorizationManager.authorize(
                AuthorizeRequest.Builder(requestContext)
                    // .addScopes(ProfileScope.profile(), ProfileScope.postalCode())
                    .addScopes(ProfileScope.profile())
                    .build()
            )
        }

        requestContext = RequestContext.create(this);
        requestContext?.registerListener(object : AuthorizeListener() {
            /*授权已成功完成。*/
            override fun onSuccess(result: AuthorizeResult) {
                /*您的应用现已获得请求范围授权*/
                val msg = "您的应用现已获得请求范围授权 accessToken:${result.accessToken}"
                Log.d(TAG, msg)
                runOnUiThread { textView.text = msg }
            }

            /*尝试授权应用时出错。*/
            override fun onError(ae: AuthError) {
                /*提示用户发生错误*/
                val msg = "提示用户发生错误"
                Log.d(TAG, msg)
                runOnUiThread { textView.text = msg }
            }

            /*授权未完成便已取消。*/
            override fun onCancel(cancellation: AuthCancellation) {
                /*将UI重新设置为随时登录状态*/
                val msg = "将UI重新设置为随时登录状态"
                Log.d(TAG, msg)
                runOnUiThread { textView.text = msg }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        requestContext?.onResume()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}