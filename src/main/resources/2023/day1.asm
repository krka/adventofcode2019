#include stdlib.asm

array[100] line
int line_len = 0

string ONE = "one"
string TWO = "two"
string THREE = "three"
string FOUR = "four"
string FIVE = "five"
string SIX = "six"
string SEVEN = "seven"
string EIGHT = "eight"
string NINE = "nine"

array[9] numbers
array[9] numbers_len
numbers[0] = ONE
numbers[1] = TWO
numbers[2] = THREE
numbers[3] = FOUR
numbers[4] = FIVE
numbers[5] = SIX
numbers[6] = SEVEN
numbers[7] = EIGHT
numbers[8] = NINE

int part1sum = 0
int part2sum = 0

func eq_num(pos, num, numlen)
  int i = 0
  while i < numlen do
    if line[pos + i] != num[i] then
      return 0
    end
    i = i + 1
  end
  return 1
end

func find(pos, step, part2)
  while pos >= 0 && pos < line_len do
    int c = line[pos]
    if c >= '0' && c <= '9' then
      return c - '0'
    elseif part2 then
      int num = 0
      while num < 9 do
        if eq_num(pos, numbers[num], numbers_len[num]) then
          return num + 1
        end
        num = num + 1
      end
    end

    pos = pos + step
  end
  throw()
end

int n = 0
while n < 9 do
  numbers_len[n] = strlen(numbers[n])
  n = n + 1
end

# TODO: fix this bug
#for n = 0, 9 do
#  numbers_len[n] = strlen(numbers[n])
##end

while 1 do
  read_until(line, 10)
  read()

  line_len = strlen(line)
  if line_len == 0 then
    break
  end


  int first = find(0, 1, 0)
  int last = find(line_len - 1, -1, 0)
  part1sum = part1sum + first * 10 + last

  first = find(0, 1, 1)
  last = find(line_len - 1, -1, 1)
  part2sum = part2sum + first * 10 + last
end

output(part1sum)
output(part2sum)
