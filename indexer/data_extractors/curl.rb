

if __FILE__ == $0

	File.new("../data_links2.txt").readlines.each do |line|


		line.strip!
		id =  line.match(/dataset\/(.*)\/version/)[1]


		cmd = "curl #{line} --max-filesize 2000000 > #{id}.json"
		p cmd
		system("#{cmd}")

	end

end