#include outputs.asm
#include stdlib.asm

int x = 0
int y = 0
int aim = 0

while 1 do
  int c = read()
  if c == 10 then
    break
  end


  skip_non_int()
  int magnitude = read_int()
  skip_until(10)

  if c == 'f' then
    x = x + magnitude
    y = y + aim * magnitude
  elseif c == 'd' then
    aim = aim + magnitude
  elseif c == 'u' then
    aim = aim - magnitude
  else
    x = -1
    y = 1
    break
  end
end

outputNumber(x * y)
output(10)
