#include division.asm
#include outputs.asm

string fizz = "Fizz"
string buzz = "Buzz"
string fizzbuzz = "FizzBuzz"

int N = input()

for i = 0, i < N do
  int r3 = 0 == i % 3
  int r5 = 0 == i % 5
  if r3 && r5 then
    outputString(fizzbuzz)
  elseif r3 then
    outputString(fizz)
  elseif r5 then
    outputString(buzz)
  else
    outputNumber(i)
  end
  output(10)
end


