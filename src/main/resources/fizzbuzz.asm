include division.asm
include outputs.asm

var i
var N
var Q
var R
var cmp
string fizz Fizz
string buzz Buzz
string fizzbuzz FizzBuzz

input N

add i 0 0
label loop
add i i 1

call div i 15 : Q R
jumpfalse R print15
call div i 5 : Q R
jumpfalse R print5
call div i 3 : Q R
jumpfalse R print3

call outputNumber i
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
