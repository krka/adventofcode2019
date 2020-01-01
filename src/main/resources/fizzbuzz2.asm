#include division.asm
#include outputs.asm

int i = 0
int offset = 0
int n = 0
int N = 0
int Q = 0
int R = 0
string fizz = "Fizz"
string buzz = "Buzz"
string fizzbuzz = "FizzBuzz"

offset = input()
N = input()

i = 0 + 0
loop:
i = i + 1
n = i + offset

Q, R = div(n, 15)
if not R jump print15
Q, R = div(n, 5)
if not R jump print5
Q, R = div(n, 3)
if not R jump print3

outputNumber(n)
output(10)
jump endloop

print15:
outputString(&fizzbuzz)
output(10)
jump endloop

print5:
outputString(&buzz)
output(10)
jump endloop

print3:
outputString(&fizz)
output(10)

endloop:
if i != N jump loop

halt
