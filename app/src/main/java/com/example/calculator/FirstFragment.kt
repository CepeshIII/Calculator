package com.example.calculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.calculator.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    var calculator = Calculator()
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)


        var bindings = listOf(binding.plus, binding.minus, binding.division,
            binding.multiplication, binding.total, binding.one, binding.two,
            binding.three, binding.four, binding.five, binding.six,
            binding.seven, binding.eight, binding.nine, binding.zero)

        bindings.forEach{
            var b = it
            it.setOnClickListener {
                calculator.push(b.text[0])
                binding.resultField.text = calculator.show()
            }
        }
        binding.clear.setOnClickListener{
            calculator.clear()
            binding.resultField.text = calculator.show()
        }
        binding.openResult.setOnClickListener{

        }


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.openResult.setOnClickListener {
           // val action = SecondFragmentDirections.actionSecondFragmentToFirstFragment("0_0")
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(calculator.result.toString())
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



class Calculator(){
    enum class State {
        START,
        FIRST_NUMBER,
        OPERATION,
        SECOND_NUMBER,
        RESULT,
        ERROR
    }

    var state = State.START
    var firstNum = ""
    var operation = '0'
    var secondNum = ""
    var result = 0.0

    fun push(c: Char){
        when(state) {
            State.START -> {
                if (c.isDigit()) {
                    firstNum += c
                    state = State.FIRST_NUMBER
                } else {
                    state = State.ERROR
                }
            }
            State.FIRST_NUMBER -> {
                when(c) {
                    in '0'..'9' -> {
                        firstNum += c
                    }
                    '+', '-', '*', '/' -> {
                        operation = c
                        state = State.OPERATION
                    }
                    else -> {
                        state = State.ERROR
                    }
                }
            }
            State.OPERATION -> {
                when(c) {
                    in '0'..'9' -> {
                        secondNum += c
                        state = State.SECOND_NUMBER
                    }
                    '+', '-', '*', '/' -> {
                        operation = c
                    }
                    else -> {
                        state = State.ERROR
                    }
                }
            }
            State.SECOND_NUMBER -> {
                when(c) {
                    in '0'..'9' -> {
                        secondNum += c
                    }
                    '=' -> {
                        state = State.RESULT
                        println(firstNum to Int )
                        var first = firstNum.toInt()
                        var second = secondNum.toInt()
                        when(operation){
                            '+' -> result = (first + second).toDouble()
                            '-' -> result = (first - second).toDouble()
                            '/' -> result = (first.toDouble() / second.toDouble())
                            '*' -> result = (first * second).toDouble()
                        }
                    }
                    else -> {
                        state = State.ERROR
                    }
                }
            }
            State.RESULT -> {}
            State.ERROR -> {}
        }
    }


    fun show(): String{

        when(state){
            State.START -> return ""
            State.FIRST_NUMBER -> return firstNum
            State.OPERATION -> return operation.toString()
            State.SECOND_NUMBER -> return secondNum
            State.RESULT -> return result.toString()
            State.ERROR -> return "ERROR"
        }
    }


    fun clear(){
        state = State.START
        firstNum = ""
        operation = '0'
        secondNum = ""
        result = 0.0
    }
}