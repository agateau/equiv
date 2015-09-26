#!/usr/bin/env python3
import argparse
import os
import sys

import pyexcel_ods3

DESCRIPTION = """\
Turns a .osd file into a set of .csv files.
"""

CELL_SEPARATOR = ';'


def write_csv(fl, sheet):
    for row in sheet:
        line = CELL_SEPARATOR.join([str(x) for x in row])
        fl.write(line)
        fl.write('\n')


def main():
    parser = argparse.ArgumentParser()
    parser.description = DESCRIPTION

    parser.add_argument(dest='ods',
        help='Read source .ods file', metavar='FILE')

    parser.add_argument(dest='output_dir',
        help='Directory where .csv files will be created')

    args = parser.parse_args()

    data = pyexcel_ods3.get_data(args.ods)

    for name, sheet in data.items():
        output_name = os.path.join(args.output_dir, name + '.csv')
        with open(output_name, 'w') as f:
            write_csv(f, sheet)

    return 0


if __name__ == '__main__':
    sys.exit(main())
# vi: ts=4 sw=4 et
