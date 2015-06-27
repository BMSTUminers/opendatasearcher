require 'net/http/persistent'
require 'uri'
require 'json'

token = "5c9ea9bcc9e71cbdc8503a3a0a01aba1"

if __FILE__ == $0
	data = File.readlines("full_links.txt")
	glob_uri = URI('http://data.gov.ru')
	th = 0
	threads = []

	http = Net::HTTP::Persistent.new 'my_app_name'
	data.each do |d|
		d.strip!
		begin
			uri_pass = URI("http://data.gov.ru/api/json/dataset/#{d.gsub("\\","")}?access_token=#{token}")
			p uri_pass
			response = http.request uri_pass

			begin
				JSON.load(response.body) # try to read json
			rescue
				throw "json"
			end

			File.write("passports/#{d}.passport.json", response.body)
			#uri = URI("http://data.gov.ru/api/json/dataset/#{d.gsub("\\","")}/version?access_token=#{token}")
			#v = JSON.load(response.body)[0]["created"]
			#puts ("http://data.gov.ru/api/json/dataset/#{d}/version/#{v}/content?access_token=#{token}")
		rescue Exception => e
			sleep 30
			http = Net::HTTP::Persistent.new 'my_app_name'
		end
	end		
	
	threads.each{|x| x.join}
end