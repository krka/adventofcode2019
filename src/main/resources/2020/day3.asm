#include outputs.asm
#include stdlib.asm
int num_entries = read_int()

int count = 0
array[100] line

for col = 0, col < num_entries do
  skip_until(10)
  read_until(line, 10)
  int len = strlen(line)
  int i = (col * 3) % len
  if line[i] == '#' then
    count = count + 1
  end
end

outputNumber(count)
output(10)
