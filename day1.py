import fileinput

sum = 0
for line in fileinput.input():
  line = line.strip()
  if line != "":
    n = int(line)
    sum += int(n / 3) - 2

print sum

