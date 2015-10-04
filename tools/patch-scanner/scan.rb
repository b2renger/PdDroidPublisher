name = ARGV[0]

if not name then
	puts "missing name argument"
	exit 1
end

Dir.glob('**/*.pd') do |file|
	nb = File.readlines(file).select{|line| line.include?(name)}.size
	if nb > 0 then
		puts "#{nb} #{name} found in #{file}"
	end
end