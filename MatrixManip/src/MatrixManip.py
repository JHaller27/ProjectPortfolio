from fractions import Fraction
from math import ceil
from collections import OrderedDict


def center_text(txt, width):
    assert (width >= len(txt)), 'Field width must be at least the length of the text'
    num_spaces = width - len(txt)
    return (' ' * (ceil(num_spaces / 2))) + txt + (' ' * (num_spaces // 2))


class Matrix:
    __slots__ = 'matrix', 'rows', 'columns'

    def __init__(self, rows, columns):
        self.rows = rows
        self.columns = columns
        self.matrix = []
        for r in range(self.rows):
            self.matrix.append([])
            for c in range(self.columns):
                self.matrix[r].append(Fraction())

    @classmethod
    def from_size_tuple(cls, size):
        return cls(size[0], size[1])

    @classmethod
    def from_matrix(cls, other):
        return cls(other.rows, other.columns) + other

    @classmethod
    def from_grid(cls, grid):
        new = cls(len(grid), len(grid[0]))
        for row in range(new.rows):
            for col in range(new.columns):
                new.matrix[row][col] = grid[row][col]
        return new

    @classmethod
    def identity(cls, size):
        new = cls(size, size)
        for i in range(size):
            new.matrix[i][i] = 1
        return new

    def fill_values(self) -> bool:
        print('Input new values...')
        row_index = 0
        while row_index < self.rows:
            row_input = input('Row ' + str(row_index + 1) + ': ')
            row_input = row_input.split(' ')
            if len(row_input) != self.columns:
                print('Format: [entry1] [entry2] ... [entryN], N = number of columns in matrix\n')
            else:
                for col_index in range(self.columns):
                    self.matrix[row_index][col_index] = Fraction(row_input[col_index])
                row_index += 1

        return True

    def same_size(self, other) -> bool:
        return (self.rows == other.rows) and (self.columns == other.columns)

    def _can_add(self, other) -> bool:
        return isinstance(other, Matrix) and self.same_size(other)

    def _can_multiply(self, other) -> bool:
        return isinstance(other, Matrix) and (self.columns == other.rows)

    def __add__(self, other):
        if self._can_add(other):
            new = Matrix(self.rows, self.columns)
            for row in range(new.rows):
                for col in range(new.columns):
                    new.matrix[row][col] = self.matrix[row][col] + other.matrix[row][col]
            return new
        else:
            print('Cannot add matrices')

    def __radd__(self, other):
        self.__add__(other)

    def __mul__(self, other):
        if isinstance(other, Fraction):
            new = Matrix(self.rows, self.columns)
            for row in range(new.rows):
                for col in range(new.columns):
                    new.matrix[row][col] = self.matrix[row][col] * other
            return new
        elif isinstance(other, Matrix):
            new = Matrix(self.rows, other.columns)
            if self._can_multiply(other):
                for i in range(self.rows):
                    for j in range(other.columns):
                        for k in range(0, self.columns):
                            new.matrix[i][j] += self.matrix[i][k] * other.matrix[k][j]
                return new
            else:
                print('Cannot multiply matrices')

    def __rmul__(self, other):
        if isinstance(other, Matrix):
            return other.__mul__(self)
        elif isinstance(other, Fraction):
            return self.__mul__(other)

    def __str__(self):
        # Find max element width
        max_width = 0
        for row in self.matrix:
            for el in row:
                if len(str(el)) > max_width:
                    max_width = len(str(el))
        max_width += 2  # Include left/right padding

        s = ''
        first = True
        for row in self.matrix:
            if first:
                first = False
            else:
                s += '\n'
            s += '['
            for el in row:
                s += center_text(str(el), max_width)
            s += ']'

        return s


class MatrixCommandParser:
    matrices = dict()
    literal_commands = dict()
    operation_commands = dict()

    def __init__(self):
        self.literal_commands = {
            'new': self.new_matrix,
            'print': lambda: print(self.__str__()),
            'help': lambda: self.print_help(),
            'fill': self.fill_matrix,
            'names': self.print_names,
            'exit': lambda: exit(),
            'del': self.remove_matrix
        }
        self.operation_commands = {
            '->': self.store_as,
            '+': self.add_matrices,
            '*': self.multiply,
            '|': self.augment
        }

    @staticmethod
    def _get_input(input_message, valid_function=None) -> str:
        raw_input = input(input_message)
        if valid_function is not None:
            while not valid_function(raw_input):
                raw_input = input(input_message)
        return raw_input

    @staticmethod
    def _is_valid_size(string_to_check: str) -> bool:
        dimensions = string_to_check.split('x')
        if len(dimensions) == 2:
            try:
                int(dimensions[0])
                int(dimensions[1])
                return True
            except ValueError:
                print('Dimensions must be integers!\n')
                return False
        else:
            print('Format: [rows]x[columns]\n')
            return False

    @staticmethod
    def _is_yn_option(string_to_check: str) -> bool:
        if not string_to_check.islower():
            print('Must be lowercase\n')
            return False
        elif string_to_check == 'y' or string_to_check== 'n':
            return True
        else:
            print("Must be either 'y' or 'n'\n")
            return False

    def _is_valid_command(self, string_to_check: str) -> bool:
        if self._is_valid_matrix(string_to_check):
            return True
        elif self._is_literal_command(string_to_check):
            return True
        elif self._is_operation_command(string_to_check):
            return True
        else:
            print('Invalid command')
            return False

    def _is_literal_command(self, string_to_check: str) -> bool:
        if string_to_check in self.literal_commands:
            return True
        else:
            return False

    def _is_operation_command(self, string_to_check: str) -> bool:
        commands_list = list(self.operation_commands.keys())
        commands_list.remove('->')
        commands_list.insert(0, '->')

        for op_char in commands_list:
            op_list = string_to_check.split(op_char)
            if len(op_list) == 2:
                return True

        print('Invalid operation')
        return False

    def _parse_operation_command(self, command_string: str) -> dict:
        temp_commands = list(self.operation_commands)
        temp_commands.remove('->')
        temp_commands.insert(0, '->')
        for op_char in temp_commands:
            op_list = command_string.split(op_char)
            if len(op_list) == 2:
                return {'left': op_list[0], 'operation': op_char, 'right': op_list[1]}

        return None

    def _is_valid_matrix(self, string_to_check: str, print_failed_message=True) -> bool:
        if string_to_check == 'cancel' or string_to_check in self.matrices or self._is_identity_name(string_to_check):
            return True
        else:
            if print_failed_message:
                print('That name does not exist. Type one of the following names or \'cancel\'')
                self.print_names()
            print()

    @staticmethod
    def _is_identity_name(matrix_name: str) -> bool:
        if matrix_name[0] == 'i':
            try:
                size = int(matrix_name[1:])
                return True
            except ValueError:
                return False

    def get_matrix(self, matrix_name: str) -> Matrix:
        if self._is_identity_name(matrix_name):
            return Matrix.identity(int(matrix_name[1:]))
        else:
            return self.matrices[matrix_name]

    def fill_matrix(self) -> None:
        name = self._get_input('Matrix name... ', self._is_valid_matrix)
        if name != 'cancel':
            self.get_matrix(name).fill_values()

    def new_matrix(self) -> None:
        name = self._get_input('Matrix name... ')
        size = self._get_input('Matrix size... ', self._is_valid_size).split('x')
        matrix = Matrix(int(size[0]), int(size[1]))

        if self._get_input('Fill matrix?.. ', self._is_yn_option) == 'y':
            matrix.fill_values()

        self.matrices[name] = matrix

    def print_help(self) -> None:
        print('Literal commands')
        cmds = list(self.literal_commands.keys())
        cmds.sort()
        for key in cmds:
            print('\t\t' + key)

        print('Valid operations')
        for key in self.operation_commands.keys():
            print('\t\t' + key)

    def parse_command(self, command_string: str) -> None:
        if self._is_valid_matrix(command_string, False):
            print(command_string)
            print(self.get_matrix(command_string))
        elif self._is_literal_command(command_string):
            self.literal_commands[command_string]()
        elif self._is_operation_command(command_string):
            command_dict = self._parse_operation_command(command_string)
            print(self.operation_commands[command_dict['operation']](command_dict['left'], command_dict['right']))

        print()

    def input_command(self) -> None:
        self.parse_command(self._get_input('>   ').lower())

    def print_names(self) -> None:
        if len(self.matrices.keys()) == 0:
            print('No matrices exist')
        else:
            chars_in_line = 0
            for name in self.matrices.keys():
                if chars_in_line >= 100:
                    print()
                    chars_in_line = 0
                print(name, end='    ')
                chars_in_line += len(name + '    ')

    def add_matrices(self, left: str, right: str) -> Matrix:
        return self.get_matrix(left) + self.get_matrix(right)

    def augment(self, left: str, right: str) -> Matrix:
        m = list.__new__(self.get_matrix(left).matrix)
        n = list(self.get_matrix(right).matrix)
        if len(m) != len(n):
            print('Matrices must have the same number of rows')
            return None

        for r in range(len(m)):
            m[r].extend(n[r])
        return Matrix.from_grid(m)

    def store_as(self, left: str, right: str) -> Matrix:
        if self._is_operation_command(left):
            command_dict = self._parse_operation_command(left)
            new = self.operation_commands[command_dict['operation']](command_dict['left'], command_dict['right'])
            self.matrices[right] = new
            return new

        return None

    def remove_matrix(self):
        name = self._get_input('Matrix name... ')
        if name in self.matrices:
            del self.matrices[name]

    def multiply(self, left: str, right: str) -> Matrix:
        try:
            left = Fraction(left)
            try:
                # scalar multiplication
                right = Fraction(right)
                return Matrix.from_grid([[left * right]])
            except ValueError:
                # left-scalar multiplication
                if self._is_valid_matrix(right):
                    return left * self.get_matrix(right)
        except ValueError:
            try:
                # right-scalar multiplication
                right = Fraction(right)
                if self._is_valid_matrix(left):
                    return self.get_matrix(left) * right
            except ValueError:
                # matrix multiplication
                if self._is_valid_matrix(left) and self._is_valid_matrix(right):
                    return self.get_matrix(left) * self.get_matrix(right)

    def __str__(self):
        if len(self.matrices) == 0:
            return 'No matrices exist'

        s = ''
        first = True
        ordered = OrderedDict(sorted(self.matrices.items()))
        for name, matrix in ordered.items():
            if first:
                first = False
            else:
                s += '\n'
            s += name + '\n'
            s += str(matrix)
        return s


def main():
    matrix_main_command()
    #matrix_test()


def matrix_test():
    m = Matrix.from_grid([[1,1,1],[2,2,2],[3,3,3]])
    r = Matrix.from_grid([[1,0,0],[0,1,0],[0,0,1]])

    print(m)
    print()
    print(r*m)


def matrix_main_command():
    parser = MatrixCommandParser()
    while True:
        parser.input_command()


if __name__ == '__main__':
    main()
