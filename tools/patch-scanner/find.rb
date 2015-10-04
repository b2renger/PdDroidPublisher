name = ARGV[0]

if not name then
	puts "missing name argument"
	exit 1
end

Dir.glob("**/#{name}") do |file|
	puts file
end