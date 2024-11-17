//package com.kuliah.greenhouse_iot.data
//
//import io.mockk.*
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.runTest
//import com.google.common.truth.Truth.assertThat
//import org.junit.Before
//import org.junit.Test
//
//@ExperimentalCoroutinesApi
//class MqttRepositoryTest {
//
//	// Mock dependencies
//	private lateinit var mqttClient: MqttClientService
//	private lateinit var repository: MqttRepository
//
//	@Before
//	fun setUp() {
//		mqttClient = mockk(relaxed = true)
//		repository = MqttRepository(mqttClient)
//	}
//
//	@Test
//	fun `verify data received from MQTT topic is parsed correctly`() = runTest {
//		// Arrange: Prepare mock data
//		val mockData = "mock_message"
//		every { mqttClient.subscribe(any(), any()) } answers {
//			secondArg<(String) -> Unit>().invoke(mockData)
//		}
//
//		// Act: Simulate receiving data
//		val result = repository.subscribeToTopic("herbalawu/monitoring")
//
//		// Assert: Verify data is parsed correctly
//		assertThat(result).isEqualTo(mockData)
//		verify { mqttClient.subscribe("herbalawu/monitoring", any()) }
//	}
//
//	@Test
//	fun `verify exception handling when subscription fails`() = runTest {
//		// Arrange: Mock failure
//		every { mqttClient.subscribe(any(), any()) } throws RuntimeException("Connection error")
//
//		// Act & Assert: Verify the exception is propagated
//		try {
//			repository.subscribeToTopic("herbalawu/monitoring")
//			assert(false) // Fail the test if no exception is thrown
//		} catch (e: Exception) {
//			assertThat(e).hasMessageThat().contains("Connection error")
//		}
//	}
//}