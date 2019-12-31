#include division.asm
#include outputs.asm

int i = 0
int offset = 0
int n = 0
int N = 0
int Q = 0
int R = 0
int cmp = 0
string fizz = "Fizz"
string buzz = "Buzz"
string fizzbuzz = "FizzBuzz"

input offset
input N

i = 0 + 0
label loop
i = i + 1
n = i + offset

call div n 15 : Q R
jumpfalse R print15
call div n 5 : Q R
jumpfalse R print5
call div n 3 : Q R
jumpfalse R print3

call outputNumber n
output 10
jumpfalse 0 endloop

label print15
call outputString &fizzbuzz
output 10
jumpfalse 0 endloop

label print5
call outputString &buzz
output 10
jumpfalse 0 endloop

label print3
call outputString &fizz
output 10

label endloop
eq cmp i N
jumpfalse cmp loop

halt
