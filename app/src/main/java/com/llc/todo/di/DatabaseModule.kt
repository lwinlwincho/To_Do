package com.llc.todo.di

import android.content.Context
import androidx.room.Room
import com.llc.todo.database.TaskDao
import com.llc.todo.database.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DatabaseModule {

    @Provides
    @ViewModelScoped
    fun provideMovieRoomDatabase(@ApplicationContext context: Context): TaskDatabase {
        return Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            "task_database"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @ViewModelScoped
    fun provideMovieDao(taskDatabase: TaskDatabase): TaskDao {
        return taskDatabase.taskDao()
    }
}