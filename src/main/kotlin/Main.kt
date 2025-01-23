package org.example

import kotlinx.coroutines.*
import org.example.TaskManager.TaskManager
import kotlin.random.Random

fun main() {
    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // 2. Task Manager 생성
    val taskManager = TaskManager(scope)

    // 3. 리스트에 작업 추가
    val tasks = listOf("Task1", "Task2", "Task3")
    taskManager.startTasks(tasks)
}

