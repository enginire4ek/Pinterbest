package com.example.pinterbest.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pinterbest.presentation.viewmodels.ActualPinViewModel
import com.example.pinterbest.presentation.viewmodels.BoardCreationViewModel
import com.example.pinterbest.presentation.viewmodels.CreatorsViewModel
import com.example.pinterbest.presentation.viewmodels.HomeViewModel
import com.example.pinterbest.presentation.viewmodels.LogInViewModel
import com.example.pinterbest.presentation.viewmodels.PinCreationViewModel
import com.example.pinterbest.presentation.viewmodels.ProfileViewModel
import com.example.pinterbest.presentation.viewmodels.SignUpViewModel
import com.example.pinterbest.presentation.viewmodels.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindsHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LogInViewModel::class)
    abstract fun bindsLogInViewModel(viewModel: LogInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    abstract fun bindsSignUpViewModel(viewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindsProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ActualPinViewModel::class)
    abstract fun bindsActualPinViewModel(viewModel: ActualPinViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PinCreationViewModel::class)
    abstract fun bindsPinCreationViewModel(viewModel: PinCreationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BoardCreationViewModel::class)
    abstract fun bindsBoardCreationViewModel(viewModel: BoardCreationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreatorsViewModel::class)
    abstract fun bindsCreatorsViewModel(viewModel: CreatorsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
