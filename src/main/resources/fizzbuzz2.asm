#include division.asm
#include outputs.asm

string fizz = "Fizz"
string buzz = "Buzz"
string fizzbuzz = "FizzBuzz"

int offset = input()
int N = input()
int n = 0

func remainder(n, d)
  int q, r = div(n, d)
  return r
end

for i = 0, i < N do
  n = i + offset
  int r3 = 0 == remainder(n, 3)
  int r5 = 0 == remainder(n, 5)
  if r3 && r5 then
    outputString(fizzbuzz)
  elseif r3 then
    outputString(fizz)
  elseif r5 then
    outputString(buzz)
  else
    outputNumber(n)
  end
  output(10)
end


