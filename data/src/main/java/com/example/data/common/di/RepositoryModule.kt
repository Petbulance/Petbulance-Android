package com.example.data.common.di

import com.example.data.remote.local.database.nonfeature.preference.PreferenceRepositoryImpl
import com.example.data.remote.local.repository.ExampleRepositoryImpl
import com.example.data.repository.feature.users.MockUsersRepository
import com.example.data.repository.nonfeature.login.LoginRepositoryImpl
import com.example.data.repository.nonfeature.login.TokenRepositoryImpl
import com.example.data.repository.nonfeature.terms.MockTermsRepository
import com.example.domain.repository.feature.ExampleRepository
import com.example.domain.repository.feature.users.UsersRepository
import com.example.domain.repository.nonfeature.login.LoginRepository
import com.example.domain.repository.nonfeature.login.TokenRepository
import com.example.domain.repository.nonfeature.terms.TermsRepository
import com.example.domain.usecase.nonfeature.preference.PreferenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExampleRepository(
        impl: ExampleRepositoryImpl
    ): ExampleRepository

    @Binds
    @Singleton
    abstract fun bindPreferenceRepository(
        impl: PreferenceRepositoryImpl
    ): PreferenceRepository

    @Binds
    abstract fun bindTokenRepository(
        tokenRepositoryImpl: TokenRepositoryImpl
    ): TokenRepository

    @Binds
    abstract fun bindLoginRepository(
        loginRepositoryImpl: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    abstract fun bindTermsRepository(
//        termsRepositoryImpl: TermsRepositoryImpl
        termsRepositoryImpl: MockTermsRepository
    ): TermsRepository

    @Binds
    abstract fun bindUserRepository(
//        usersRepositoryImpl: UsersRepositoryImpl,
        usersRepositoryImpl: MockUsersRepository
    ): UsersRepository
}