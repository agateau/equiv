#!/usr/bin/env python3
import argparse
import os
import sys

import pyexcel_ods3

DESCRIPTION = """\
Turns a .ods file into a set of .csv files.
"""

CELL_SEPARATOR = ';'


def write_csv(fl, sheet, skipped_rows={}, skipped_columns={}):
    for row_idx, row in enumerate(sheet):
        if (row_idx + 1) in skipped_rows:
            continue
        cells = [str(x) for col_idx, x in enumerate(row) if (col_idx + 1) not in skipped_columns]
        line = CELL_SEPARATOR.join(cells)
        fl.write(line)
        fl.write('\n')


def main():
    parser = argparse.ArgumentParser()
    parser.description = DESCRIPTION

    parser.add_argument(dest='ods',
        help='Read source .ods file', metavar='FILE')

    parser.add_argument(dest='output_dir',
        help='Directory where .csv files will be created')

    parser.add_argument('--skip-row', action='append', type=int,
        help='Skip row with index IDX', metavar='IDX')

    parser.add_argument('--skip-column', action='append', type=int,
        help='Skip column with index IDX', metavar='IDX')

    args = parser.parse_args()

    data = pyexcel_ods3.get_data(args.ods)

    for name, sheet in data.items():
        output_name = os.path.join(args.output_dir, name + '.csv')
        with open(output_name, 'w') as f:
            skipped_rows = set(args.skip_row)
            skipped_columns = set(args.skip_column)
            print(skipped_rows)
            print(skipped_columns)
            write_csv(f, sheet, skipped_rows=skipped_rows, skipped_columns=skipped_columns)

    return 0


if __name__ == '__main__':
    sys.exit(main())
# vi: ts=4 sw=4 et
