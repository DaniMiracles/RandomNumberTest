package com.example.testjunior.main.numbers.data


interface NumbersCloudDataSource : FetchNumber {

    suspend fun randomNumberFact(): NumberData

    class Base(
        private val service: NumbersService,
    ) : NumbersCloudDataSource {

        override suspend fun randomNumberFact(): NumberData {
            val response = service.random()
            val body = response.body() ?: throw IllegalStateException("Service Unavailable")
            val headers = response.headers()

            headers.find { (key, value) ->
                key == RANDOM_API_HEADER
            }?.let { (_, value) ->
                return NumberData(value, body)
            }
            throw java.lang.IllegalStateException("Service Unavailable")
        }

        override suspend fun numberFact(number: String): NumberData {
            val fact = service.fact(number)
            return NumberData(number, fact)
        }

        companion object {
            private const val RANDOM_API_HEADER = "X-Numbers-API-Number"
        }

    }

}