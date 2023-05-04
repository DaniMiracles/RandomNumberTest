package com.example.testjunior.main.presentation


import com.example.testjunior.main.numbers.domain.NumberFact
import com.example.testjunior.main.numbers.domain.NumberUiMapper
import com.example.testjunior.main.numbers.domain.NumbersInteractor
import com.example.testjunior.main.numbers.domain.NumbersResult
import com.example.testjunior.main.numbers.presentation.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class NumbersViewModelTest : BaseTest() {

    /**
     * Initial test
     * At start fetch data and show it
     * then try to get some data successfully
     * then re-init and check the result
     */

    private lateinit var communication: TestNumbersCommunication
    private lateinit var interactor: TestNumbersInteractor
    private lateinit var viewModel: NumbersViewModel
    private lateinit var manageResources: TestManageResources

    @Before
    fun init_tests() {
        manageResources = TestManageResources()
        communication = TestNumbersCommunication()
        interactor = TestNumbersInteractor()


        viewModel = NumbersViewModel(
            manageResources,
            communication,
            interactor,
            HandleNumbersRequest.Base(
                communication,
                TestDispatchersList(),
                NumbersResultMapper(communication, NumberUiMapper())
            )
        )
    }

    @Test
    fun testInitAndReInit() = runBlocking {


        //1.Initialize
        interactor.changeExpectedResult(NumbersResult.Success())
        //2.Action
        viewModel.init(isFirstRun = true)
        //3.Check

        assertEquals(true, communication.progressCalledList[0])
        assertEquals(2, communication.progressCalledList.size)
        assertEquals(false, communication.progressCalledList[1])

        assertEquals(1, communication.stateCalledList.size)
        assertEquals(UiState.Success(), communication.stateCalledList[0])



        assertEquals(0, communication.numbersList.size)
        assertEquals(0, communication.timesShowList)


        //get some data
        interactor.changeExpectedResult(NumbersResult.Failure("no internet connection"))
        viewModel.fetchRandomNumberFact()


        assertEquals(true, communication.progressCalledList[2])

        assertEquals(1, interactor.fetchAboutRandomNumberCalledList.size)

        assertEquals(4, communication.progressCalledList.size)
        assertEquals(false, communication.progressCalledList[3])

        assertEquals(2, communication.stateCalledList.size)
        assertEquals(UiState.Error("no internet connection"), communication.stateCalledList[1])

        assertEquals(0, communication.timesShowList)

        viewModel.init(isFirstRun = false)
        assertEquals(4, communication.progressCalledList.size)
        assertEquals(2, communication.stateCalledList.size)
        assertEquals(0, communication.timesShowList)
    }

    /**
     * Try to get information about empty number
     */
    @Test
    fun factAboutEmptyNumber() = runBlocking {
        manageResources.errorMessage = "entered number is empty"
        viewModel.fetchNumberFact("")
        assertEquals(0, interactor.fetchAboutNumberCalledList.size)
        assertEquals(0, communication.progressCalledList.size)

        assertEquals(1, communication.stateCalledList.size)

        assertEquals(UiState.Error("entered number is empty"), communication.stateCalledList[0])

        assertEquals(0, communication.timesShowList)


    }

    /**
     * Try to get information about some number
     */
    @Test
    fun factAboutSomeNumber() = runBlocking {


        interactor.changeExpectedResult(
            NumbersResult.Success(listOf(NumberFact("67", "interesting fact about 67")))
        )

        viewModel.fetchNumberFact("67")


        assertEquals(true, communication.progressCalledList[0])

        assertEquals(1, interactor.fetchAboutNumberCalledList.size)
        assertEquals(
            NumbersResult.Success(listOf(NumberFact("67", "interesting fact about 67"))),
            interactor.fetchAboutNumberCalledList[0]
        )

        assertEquals(2, communication.progressCalledList.size)
        assertEquals(false, communication.progressCalledList[1])

        assertEquals(1, communication.stateCalledList.size)
        assertEquals(UiState.Success(), communication.stateCalledList[0])


        assertEquals(1, communication.timesShowList)
        assertEquals(NumberUi("67", "interesting fact about 67"), communication.numbersList[0])

    }


    private class TestManageResources : ManageResources {
        var errorMessage = ""

        override fun addString(id: Int): String = errorMessage

    }


    private class TestNumbersInteractor : NumbersInteractor {
        val initCalledList = mutableListOf<NumbersResult>()
        private var result: NumbersResult = NumbersResult.Success()
        val fetchAboutNumberCalledList = mutableListOf<NumbersResult>()
        val fetchAboutRandomNumberCalledList = mutableListOf<NumbersResult>()

        fun changeExpectedResult(newResult: NumbersResult) {
            result = newResult
        }

        override suspend fun init(): NumbersResult {
            initCalledList.add(result)
            return result
        }

        override suspend fun factAboutNumber(number: String): NumbersResult {
            fetchAboutNumberCalledList.add(result)
            return result
        }

        override suspend fun factAboutRandomNumber(): NumbersResult {
            fetchAboutRandomNumberCalledList.add(result)
            return result
        }
    }


    private class TestDispatchersList : DispatchersList {
        @OptIn(ExperimentalCoroutinesApi::class)
        override fun io(): CoroutineDispatcher = TestCoroutineDispatcher()

        @OptIn(ExperimentalCoroutinesApi::class)
        override fun ui(): CoroutineDispatcher = TestCoroutineDispatcher()


    }

}