#include division.asm
#include outputs.asm

int i = 0
int N = 0
int Q = 0
int R = 0
int cmp = 0
string fizz = "Fizz"
string buzz = "Buzz"
string fizzbuzz = "FizzBuzz"

input N

i = 0 + 0
label loop
i = i + 1

call div i 15 : Q R
if not R jump print15
call div i 5 : Q R
if not R jump print5
call div i 3 : Q R
if not R jump print3

call outputNumber i
output 10
jump endloop

label print15
call outputString &fizzbuzz
output 10
jump endloop

label print5
call outputString &buzz
output 10
jump endloop

label print3
call outputString &fizz
output 10

label endloop
cmp = i == N
if not cmp jump loop

halt
