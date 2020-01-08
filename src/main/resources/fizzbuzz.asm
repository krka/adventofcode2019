#include division.asm
#include outputs.asm

int i = 0
int N = 0
int Q = 0
int R = 0
string fizz = "Fizz"
string buzz = "Buzz"
string fizzbuzz = "FizzBuzz"

N = input()

loop:
i = i + 1

Q, R = div(i, 15)
if ! R jump print15
Q, R = div(i, 5)
if ! R jump print5
Q, R = div(i, 3)
if ! R jump print3

outputNumber(i)
output(10)
jump endloop

print15:
outputString(fizzbuzz)
output(10)
jump endloop

print5:
outputString(buzz)
output(10)
jump endloop

print3:
outputString(fizz)
output(10)

endloop:
if i != N jump loop

halt()
