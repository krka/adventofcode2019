#include outputs.asm
#include stdlib.asm

array[20] buf
array[2000 * 20] lines
array[2000] filter

int n = 0

while 1 do
  read_until(buf, 10)
  skip_until(10)
  int len = strlen(buf)
  if len == 0 then
    break
  end
  memcpy(len + 1, buf, lines + 20 * n)
  n = n + 1
end

func count1(index)
  int c = 0
  for i = 0, i < n do
    if (filter[i] == 0) && (lines[20 * i + index] == '1') then
      c = c + 1
    end
  end
  return c
end

func calc(flag)
  for i = 0, i < n do
    filter[i] = 0
  end
  int index = 0
  int remaining = n
  while remaining > 1 do
    int ones = count1(index)
    int zeros = remaining - ones
    int keep = '0'
    if (ones >= zeros) == flag then
      keep = '1'
    end


    for i = 0, i < n do
      if filter[i] == 0 then
        int val = lines[20 * i + index]
        if val != keep then
          filter[i] = 1
          remaining = remaining - 1
        end
      end
    end
    index = index + 1
  end

  for i = 0, i < n do
    if filter[i] == 0 then
      int res = 0
      for j = 0, j < 20 do
        int val = lines[20 * i + j]
        if val == 0 then
          return res
        else
          res = res * 2 + (val - '0')
        end
      end
      return res
    end
  end

  # unreachable
  return -1
end

int oxygen = calc(1)
int scrubber = calc(0)

outputNumber(oxygen * scrubber)
output(10)
