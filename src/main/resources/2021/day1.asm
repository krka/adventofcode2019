#include outputs.asm
#include stdlib.asm

int p1 = -1
int p2 = -1
int p3 = -1
int count = 0
while 1 do
  if peek() == 10 then
    break
  end

  int val = read_int()
  skip_until(10)

  if p3 > -1 && val > p3 then
    count = count + 1
  end

  p3 = p2
  p2 = p1
  p1 = val
end

outputNumber(count)
output(10)
