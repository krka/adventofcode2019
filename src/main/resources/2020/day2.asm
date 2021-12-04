#include outputs.asm
#include stdlib.asm
int num_entries = read_int()

int count = 0
array[2] c
array[1000] pw

for case = 0, case < num_entries do

  skip_non_int()
  int min = read_int()

  skip_non_int()
  int max = read_int()

  skip_non_chars()
  read_chars(c, 1)

  skip_non_chars()
  read_chars(pw, 999)

  int c0 = c[0]
  int matches = 0
  int len = strlen(pw)
  for i = 0, i < len do
    if pw[i] == c0 then
      matches = matches + 1
    end
  end
  if matches >= min && matches <= max then
    count = count + 1
  end
end

outputNumber(count)
output(10)
