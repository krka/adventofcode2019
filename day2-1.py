from copy import deepcopy
import fileinput

source = []
for line in fileinput.input():
  numbers = line.strip().split(",")
  source += [int(x) for x in numbers if x != '']

def compute(a, b):
  program = deepcopy(source)
  program[1] = a
  program[2] = b

  i = 0
  while True:
    opcode = program[i]
    if opcode == 1:
      x = program[program[i + 1]]
      y = program[program[i + 2]]
      dest = program[i + 3]
      program[dest] = x + y
      i += 4;
    if opcode == 2:
      x = program[program[i + 1]]
      y = program[program[i + 2]]
      dest = program[i + 3]
      program[dest] = x * y
      i += 4;
    if opcode == 99:
      break
  return program[0]

def find():
  for i in range(0, 100):
    for j in range(0, 100):
      if compute(i, j) == 19690720:
        return 100*i + j
      
print find()

