#include division.asm
#include outputs.asm

string fizz = "Fizz"
string buzz = "Buzz"
string fizzbuzz = "FizzBuzz"

int N = input()

func remainder(n, d)
  int q, r = div(n, d)
  return r
end

int i = 0
while i < N
  i = i + 1
  int r3 = 0 == remainder(i, 3)
  int r5 = 0 == remainder(i, 5)
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


