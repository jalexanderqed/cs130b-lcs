#!/usr/bin/perl -w

if ($#ARGV < 2) {
	print "Usage: $0 out-file gold (small|large|medium) [all]\n";
	exit;
}

if ( -f $ARGV[0] ) {
	open(OUT, "$ARGV[0]");
} else {
	print "0";
	exit;
}

if ( -f $ARGV[1] ) {
	open(GOLD, "$ARGV[1]");
} else {
	print "0";
	exit;
}

@out = <OUT>;
@gold = <GOLD>;
$tcase=$ARGV[2];

$numCases = 0;
@matches = (0);
@total = (0);

$maxScore = 0;
$yourScore = 0;

if ($#ARGV < 3) {
	$maxScore = $#gold + 1;
	for ($i = 0; $i <= $#gold; $i++) {
		$gline = $gold[$i];
		if ($i<= $#out) {
			$line = $out[$i];
			if ($tcase ne "small" ) {
				($line) = split (/\s+/, $line, 2);
				($gline) = split (/\s+/, $gline, 2);
			}

			if ($gline eq $line) {
				$yourScore++;
			}
		}
	}
}
else {
	# Comparing LCS all
	$start = 0;
	$end = 0;
	while ($end <= $#out && $out[$end] !~ m/^\s*$/) { 
		$end++; 
	}
	for ($i = 0; $i <= $#gold; $i++) {
		$gline = $gold[$i];
		if ($gline =~ m/^\s*$/) {
			$start = ++$end;
			while ($end <= $#out && $out[$end] !~ m/^\s*$/) {
				$end++; 
			}
			$numCases++;
			push(@matches, 0);
			push(@total, 0);
		}
		else {
			$total[$numCases]++;
			for ($k = $start; $k < $end; $k++) {
				if ("$out[$k]" eq "$gline") {
					$matches[$numCases]++;
				}
			}
		}
	}

	for ($i = 0; $i <= $#matches; $i++) {
		$maxScore += $total[$i];
		$yourScore += $matches[$i];
	}
}
	
printf("%.4f",$yourScore * 1.0 / $maxScore);
#print "$yourScore $maxScore";
