package br.com.iteris.productslist.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import br.com.iteris.productslist.databinding.ActivityUserProfileBinding
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class UserProfileActivity : UserBaseActivity() {

    private val binding : ActivityUserProfileBinding by lazy { ActivityUserProfileBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            user.filterNotNull().collect {
                with(binding) {
                    userProfileTvName.text = it.name
                    userProfileTvUsername.text = it.id

                    userProfileBtnLogout.setOnClickListener {
                        launch {
                            logout()
                        }
                    }
                }
            }
        }

    }


}